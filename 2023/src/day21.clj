(ns day21
  (:require
   [core :refer [get-puzzle-input]]))

(def input (get-puzzle-input 21))
(def char-map (->> input
                   (map-indexed (fn [row line]
                                  (map-indexed (fn [col c]
                                                 [[row col] c])
                                               line)))
                   (apply concat)
                   (into {})))
(def start (->> char-map
                (filter #(= \S (second %)))
                ffirst))
(def offsets [[0 1] [0 -1] [1 0] [-1 0]])

;; part 1
(loop [locs #{start}
       n 0]
  (if (< n 64)
    (recur (->> locs
                (map (fn [[x y]]
                       (map (fn [[x-off y-off]]
                              [(+ x x-off)
                               (+ y y-off)])
                            offsets)))
                (apply concat)
                (filter #(not= (char-map %)
                            \#))
                (into #{}))
           (inc n))
    (count locs)))

