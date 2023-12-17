(ns day12
  (:require
   [clojure.string :as str]
   [core :refer [get-puzzle-input fn-m]]))

(defn parse-line [line] (-> (str/split line #" ")
                            (update 1 #(->> (str/split % #",")
                                            (mapv parse-long)))))

(defn ans [dots blocks]
  (let
   [len-blocks (count blocks)
    f (fn-m f [i bi current]
            (let [dot-i (when (< i (count dots)) (get dots i))]
              (if (= i (count dots))
                (cond (and (= bi len-blocks)
                           (= current 0))
                      1
                      (and (= bi (dec len-blocks))
                           (= current (get blocks bi)))
                      1
                      :else
                      0)
                (->> [\. \#]
                     (map #(if (or (= dot-i %)
                                   (= dot-i \?))
                             (cond (and (= % \.)
                                        (= current 0))
                                   (f (inc i) bi 0)

                                   (and (= % \.)
                                        (> current 0)
                                        (< bi len-blocks)
                                        (= (get blocks bi) current))
                                   (f (inc i) (inc bi) 0)

                                   (= % \#)
                                   (f (inc i) bi (inc current))

                                   :else 0)
                             0))
                     (reduce +)))))]
    (f 0 0 0)))

;; part 1
(->> (get-puzzle-input 12)
     (map parse-line)
     (map #(ans (first %) (second %)))
     (reduce +))

;; part 2
(->> (get-puzzle-input 12)
     (map parse-line)
     (mapv (fn [[s cnts]]
             [(str/join (interpose "?" (repeat 5 s)))
              (vec (apply concat (repeat 5 cnts)))]))
     (pmap #(ans (first %) (second %)))
     (reduce +))

