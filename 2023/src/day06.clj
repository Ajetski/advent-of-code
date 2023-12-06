(ns ^{:doc "Day 6"
      :author "Adam Jeniski"}
 day06 (:require [clojure.string :as string]
                 [core :refer [get-puzzle-input]]))

(def input (get-puzzle-input 6))

(defn solve [num-str-lists] ; takes a list of list of nums-string representing time and distance
  (->> (map (partial map #(Long/parseLong %)) num-str-lists)
       (apply zipmap)
       (map (fn [[ms record]] (->> (range 1 ms)
                                   (map #(* % (- ms %)))
                                   (filter #(> % record))
                                   count)))
       (reduce *)))

;; part 1
(solve (map (partial re-seq #"\d+") input))

;; part 2
(solve (->> (map (partial re-seq #"[\s\d]+") input)
            (map (partial map #(string/replace % " " "")))))

