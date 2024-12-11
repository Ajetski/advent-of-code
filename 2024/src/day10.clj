(ns day10
  (:require [core :as c]
            [input-manager :refer [get-input]]))

(def input (update-vals (c/map-by-coords (get-input 10))
                        #(Character/getNumericValue %)))

(def trailheads (->> input
                     (filter #(= 0 (second %)))
                     (map first)))

(defn possible-steps [loc]
  (let [c (input loc)]
    (->> [(update loc 0 inc)
          (update loc 0 dec)
          (update loc 1 inc)
          (update loc 1 dec)]
         (filter #(= (input %) (inc c))))))

(defn get-score [trailhead]
  (->> (loop [[loc & locs] [trailhead]
              seen #{trailhead}]
         (if (nil? loc)
           seen
           (let [steps (filter (comp not seen)
                               (possible-steps loc))]
             (recur (into locs steps)
                    (into seen steps)))))
       (filter #(= 9 (input %)))
       count))

;; part 1
(->> trailheads
     (map get-score)
     (reduce +))

(defn get-trails [trailhead]
  (->> (loop [[[loc path] & locs] [[trailhead []]]
              trails #{}]
         (cond
           (nil? loc) trails

           (= (input loc) 9)
           (recur locs (conj trails path))

           :else
           (let [steps (map (juxt identity #(conj path %))
                            (possible-steps loc))]
             (recur (into locs steps)
                    trails))))))

;; part 2
(->> trailheads
     (map get-trails)
     (map count)
     (reduce +))
