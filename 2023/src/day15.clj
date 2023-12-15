(ns day15 (:require [clojure.string :as str]
                    [core :refer [get-puzzle-input]]))

(def input (-> (first (get-puzzle-input 15))
               (str/replace #"\n" "")
               (str/split #",")))

(defn HASH [s]
  (reduce (fn [acc el] (mod (* 17 (+ el acc)) 256))
          0
          (map int s)))

;; part 1
(->> (map HASH input)
     (reduce +))

;; part 2
(->> input
     (map (juxt #(re-find #"[a-zA-Z]+" %)
                #(re-find #"[0-9]+" %)))
     (map #(->> (first %) HASH (assoc % 2)))
     (reduce (fn [m [label lens box]]
               (if lens
                 (if (some #(= (first %) label)
                           (m box))
                   (update m box (partial mapv #(if (= (first %) label)
                                                  [label lens]
                                                  %)))
                   (update m box conj [label lens]))
                 (update m box (partial filterv #(not= (first %) label)))))
             (->> (range 5)
                  (map #(vector % []))
                  (into {})))
     (map (fn [[box l]]
            (map-indexed (fn [idx el]
                           (* (inc idx)
                              (inc box)
                              (parse-long (second el))))
                         l)))
     (apply concat)
     (reduce +))
