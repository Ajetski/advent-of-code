(ns ^{:doc "Day 6"
      :author "Adam Jeniski"}
 day06 (:require [clojure.string :as string]
                 [core :refer [get-puzzle-input]]))

(def input (->> (get-puzzle-input 6)))

(defn ways-to-beat [[ms record]]
  (->> (range 1 ms)
       (pmap #(* % (- ms %)))
       (filter #(> % record))
       (count)))

;; part 1
(->> input
     (map (partial re-seq #"\d+"))
     (map (partial map #(Long/parseLong %)))
     (apply zipmap)
     (map ways-to-beat)
     (reduce *))

;; part 2
(->> input
     (map (partial re-seq #"[\s\d]+"))
     (map (partial map #(string/replace % " " "")))
     (map (partial map #(Long/parseLong %)))
     (apply zipmap)
     (map ways-to-beat)
     (reduce *))

