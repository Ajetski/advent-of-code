(ns day04
  (:require [input-manager :refer [get-input]]
            [core :as c]
            [clojure.string :as str]))

(def input (get-input 4))

(defn get-char [[row col]]
  (get (get input row) col))

;; part 1
(->> (c/get-coords input)
     (map (fn [[row col]]
            (->> (for [offset (range 4)]
                   [[row (+ col offset)]
                    [row (- col offset)]
                    [(+ row offset) col]
                    [(- row offset) col]
                    [(- row offset) (- col offset)]
                    [(- row offset) (+ col offset)]
                    [(+ row offset) (- col offset)]
                    [(+ row offset) (+ col offset)]])
                 (c/mmap get-char)
                 (apply map vector)
                 (filter #(= % (seq "XMAS")))
                 count)))
     (reduce +))

;; part 2
(->> (c/get-coords input)
     (filter #(= (get-char %) \A))
     (filter (fn [[row col]]
               (->> [[[(dec row) (dec col)] [(inc row) (inc col)]]
                     [[(inc row) (dec col)] [(dec row) (inc col)]]]
                    (c/mmap get-char)
                    (map set)
                    (apply = #{\M \S}))))
     count)

