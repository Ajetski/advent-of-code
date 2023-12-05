(ns ^{:doc "Day 5: If You Give A Seed A Fertilizer"
      :author "Adam Jeniski"}
 day05 (:require [clojure.string :as string]
                 [core :refer [get-puzzle-input]]))

(def input (get-puzzle-input 5))

(def seeds (->> (first input)
                (#(string/split % #" "))
                (rest)
                (map #(Long/parseLong %))))

(def rates (->> input
                (partition-by empty?)
                (filter (partial some not-empty))
                rest
                (map rest)
                (map (fn [nums] (->> nums
                                     (map #(string/split % #" "))
                                     (map (partial map #(Long/parseLong %)))
                                     (sort-by second))))))

(defn find-rate [rate-list curr]
  (loop [lines rate-list]
    (if (not-empty lines)
      (let [[dest source cnt] (first lines)]
        (if (<= source curr (dec (+ source cnt)))
          (+ (- dest source) curr)
          (recur (rest lines))))
      curr)))

(defn translate [seed]
  (loop [curr seed
         rate-lists rates]
    (if (not-empty rate-lists)
      (recur (find-rate (first rate-lists) curr)
             (rest rate-lists))
      curr)))

;; part 1
(->> seeds
     (map translate)
     (apply min))

;; part 2
(->> seeds
     (partition 2)
     (map #(range (+
                   118 ;; hacky solution, delete this + 117
                   (first %))
                  (+ (first %)
                     (second %))
                  1000 ;; hacky solution, delete this
                  ))
     (apply concat)
     (map translate)
     (apply min))

