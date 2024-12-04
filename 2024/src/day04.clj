(ns day04
  (:require [input-manager :refer [get-input]]))

(def input (get-input 4))

(defn get-char [row col]
  (get (get input row) col))

;; part 1
(->> input
     (map (partial map-indexed vector))
     (map-indexed vector)
     (reduce (fn [acc [row s]]
               (reduce (fn [acc2 [col _c]]
                         (+ acc2 (->> (for [offset (range 4)]
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
                       acc
                       s))
             0))

;; part 2
(->> input
     (map reverse)
     (map (partial map-indexed vector))
     (map-indexed vector)
     (reduce (fn [acc [row s]]
               (reduce (fn [acc2 [col _c]]
                         (+ acc2 (if (= (get-char row col) \A)
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
                                   0)))
                       acc
                       s))
             0)
     ;
     )

