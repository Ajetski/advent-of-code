(ns day04
  (:require [input-manager :refer [get-input]]
            [core :as c]
            [clojure.string :as str]))

(def input (get-input 4))

(defn get-char [row col]
  (get (get input row) col))

;; part 1
(->> input
     c/get-coords
     (map (fn [[row col]]
          (->> (for [offset (range 4)]
                         (map #(apply get-char %)
                              [[row (+ col offset)]
                               [row (- col offset)]
                               [(+ row offset) col]
                               [(- row offset) col]
                               [(- row offset) (- col offset)]
                               [(- row offset) (+ col offset)]
                               [(+ row offset) (- col offset)]
                               [(+ row offset) (+ col offset)]]))
                       (apply map vector)
                       (filter #(= % (seq "XMAS")))
                       count)))
     (reduce +))

;; part 2
(->> input
     c/get-coords
     (filter #(= (apply get-char %) \A))
     (map (fn [[row col]]
            (->> [[[(dec row) (dec col)] [(inc row) (inc col)]]
                  [[(inc row) (dec col)] [(dec row) (inc col)]]]
                 (map (partial map #(apply get-char %)))
                 (map set)
                 (apply = #{\M \S})
                 c/bool->binary)))
     (reduce +))

