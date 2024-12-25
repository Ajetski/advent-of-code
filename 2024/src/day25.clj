(ns day25
  (:require
   [clojure.string :as s]
   [core :refer :all :as c]
   [input-manager :refer :all]))

(def input (->> (split-on-double-newlines (get-input-raw 2024 25))
                (map s/split-lines)))

(defn get-counts [str-rows]
  (->> (apply map vector str-rows) ;; flip grid over y=x axis
       (map (partial filter #(= \# %)))
       (map count)))

(def key-counts (->> input
                     (filter #(every? (partial = \.) (first %)))
                     (map get-counts)))

(def lock-counts (->> input
                      (filter #(every? (partial = \#) (first %)))
                      (map get-counts)))

(def space (count (first input)))

;; part 1
(->> (for [k key-counts
           l lock-counts]
       (map + k l))
     (filter (partial every? #(<= % space)))
     count)

;; classic, no part 2. just get the rest of the stars!

