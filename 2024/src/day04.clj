(ns day04
  (:require [core :as c]
            [input-manager :refer [get-input]]
            [clojure.string :as str]))

(do
  (def input
    (->>
     (get-input 4)
; "MMMSXXMASM
; MSAMXMSMSA
; AMXSXMAAMM
; MSAMASMSMX
; XMASAMXAMM
; XXAMMXXAMA
; SMSMSASXSS
; SAXAMASAAA
; MAMMMXMMMM
; MXMXAXMASX"
; (str/split-lines)
     ))
  input)

(defn get-char [row col]
  (get (get input row) col))

(defn get-count [row col]
  (->> [(for [offset (range 4)]
          (get-char row (+ col offset))) ;right
        (for [offset (range 4)]
          (get-char row (- col offset))) ;left
        (for [offset (range 4)]
          (get-char (+ row offset) col)) ;down
        (for [offset (range 4)]
          (get-char (- row offset) col)) ;up
        (for [offset (range 4)]
          (get-char (- row offset) (- col offset)))
        (for [offset (range 4)]
          (get-char (- row offset) (+ col offset)))
        (for [offset (range 4)]
          (get-char (+ row offset) (- col offset)))
        (for [offset (range 4)]
          (get-char (+ row offset) (+ col offset)))]
       (filter #(= % (seq "XMAS")))
       count))

;; part 1
(->> input
     (map (partial map-indexed vector))
     (map-indexed vector)
     (reduce (fn [acc [row s]]
               (reduce (fn [acc2 [col _c]]
                         (+ acc2 (get-count row col)))
                       acc
                       s))
             0)
     ;
     )

(defn get-count-2 [row col]
  (if (= (get-char row col) \A)
    (let [corners [[(dec row) (dec col)]
                   [(inc row) (dec col)]
                   [(dec row) (inc col)]
                   [(inc row) (inc col)]]
          chars (mapv #(apply get-char %) corners)]
      (if (and (= 2
             (count (filter #(= % \S) chars))
             (count (filter #(= % \M) chars)))
               (not= (get chars 0)
                     (get chars 3)))
        1
        0))
    0))

;; part 2
(->> input
     (map reverse)
     (map (partial map-indexed vector))
     (map-indexed vector)
     (reduce (fn [acc [row s]]
               (reduce (fn [acc2 [col _c]]
                         (+ acc2 (get-count-2 row col)))
                       acc
                       s))
             0)
     ;
     )

