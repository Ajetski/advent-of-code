(ns ^{:doc "Day 2"
      :author "Adam Jeniski"}
 day02 (:require [core :refer [get-puzzle-input]]
                 [clojure.string :as string]))

(defn parse-line [line]
  (let [[_ id data] (re-find #"Game (\d+): (.*)" line)
        states (->> (string/split data #";")
                    (map #(map string/trim (string/split % #",")))
                    (map #(map (fn [s]
                                 (let [[num color] (string/split s #" ")]
                                   [(Integer/parseInt num) color])) %)))]
    [id states]))

(def lines (->> (get-puzzle-input 2)
                (map parse-line)))

(def cube-counts {"red" 12, "green" 13, "blue" 14})

;; part 1
(->> lines
     (filter (fn [[_id states]]
               (not (some
                     #(some (fn [[cnt color]]
                              (< (cube-counts color) cnt)) %)
                     states))))
     (map (fn [[id]] (Integer/parseInt id)))
     (reduce +))

;; part 2
(->> lines
     (map (fn [[_id states]]
            (reduce #(reduce (fn [acc [cnt color]]
                               (update acc color max cnt))
                             %1
                             %2)
                    {"green" 0, "red" 0, "blue" 0}
                    states)))
     (map #(* (% "green")
              (% "red")
              (% "blue")))
     (reduce +))
