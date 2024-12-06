(ns day06
  (:require [input-manager :refer [get-input]]
            [core :as c]))

(def input (mapv vec (get-input 6)))

(defn get-char [[row col]]
  (get (get input row) col))

(def start (->> (c/get-coords input)
                (filter #(= \^ (get-char %)))
                first))

(def num-cols (count (first input)))
(def num-rows (count input))

(defn move [dir [row col]]
  (condp = dir
    :up [(dec row) col]
    :down [(inc row) col]
    :left [row (dec col)]
    :right [row (inc col)]))

(defn turn-right [dir]
  (condp = dir
    :up :right
    :right :down
    :down :left
    :left :up))

(defn in-bounds? [[row col]]
  (and (>= row 0) (>= col 0) (< row num-rows) (< col num-cols)))

(defn get-path []
  (loop [pos start
         dir :up
         path #{}]
    (cond
      (not (in-bounds? pos)) path
      (path [dir pos]) :cycle
      :else (let [pos' (move dir pos)]
              (if (= (get-char pos') \#)
                (recur pos (turn-right dir) (conj path [dir pos]))
                (recur pos' dir (conj path [dir pos])))))))

(defn add-obstacle [[row col]]
  (update input row assoc col \#))

;; part 1
(->> (get-path)
     (map second)
     distinct
     count)

;; part 2
(->> (for [pos (->> (get-path)
                    (map second)
                    distinct
                    (filter #(not= % start)))]
       (with-redefs [input (add-obstacle pos)] (get-path)))
     (filter #(= % :cycle))
     count)

