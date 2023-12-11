(ns day11
  (:require [core :refer [get-puzzle-input]]))

(def input (get-puzzle-input 11))

(def empty-rows
  (->> input
       (map-indexed vector)
       (filter (fn [[_ line]]
                 (every? #(= \. %) line)))
       (map first)))

(def empty-cols
  (->> input first count range
       (filter (fn [n] (every? #(= \. %)
                               (map #(.charAt % n) input))))))

(defn gen-hash-locations [modifier]
  (->> input
       (map-indexed (fn [row line]
                      (map-indexed (fn [col c]
                                     [[row col] c])
                                   line)))
       (apply concat)
       (filter #(= \# (second %)))
       (map first)
       (map (fn [[row col]]
              [(+ row (* (dec modifier)
                         (count (filter #(> row %) empty-rows))))
               (+ col (* (dec modifier)
                         (count (filter #(> col %) empty-cols))))]))))

;; part 1
(let [locs (gen-hash-locations 2)]
  (->> (into #{} (for [[x1 y1 :as a] locs
                       [x2 y2 :as b] locs
                       :when (or (not= x1 x2)
                                 (not= y1 y2))]
                   (sort [a b])))
       (map (fn [[[x1 y1] [x2 y2]]]
              (+ (abs (- x2 x1)) (abs (- y2 y1)))))
       (reduce +)))

;; part 2
(let [locs (gen-hash-locations 1000000)]
  (->> (into #{} (for [[x1 y1 :as a] locs
                       [x2 y2 :as b] locs
                       :when (or (not= x1 x2)
                                 (not= y1 y2))]
                   (sort [a b])))
       (map (fn [[[x1 y1] [x2 y2]]]
              (+ (abs (- x2 x1)) (abs (- y2 y1)))))
       (reduce +)))
