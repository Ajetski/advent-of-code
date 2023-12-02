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
     (filter
      (fn [[_id games]]
        (not (some (fn [game]
                     (some (fn [[cnt color]]
                             (< (cube-counts color) cnt))
                           game))
                   games))))
     (map (fn [[id]] (Integer/parseInt id)))
     (reduce +))

;; part 2
(->> lines
     (map (fn [[_id games]]
            (reduce (fn [acc game]
                      (reduce (fn [inner-acc [cnt color]]
                                (update inner-acc color max cnt))
                              acc
                              game))
                    {"green" 0, "red" 0, "blue" 0}
                    games)))
     (map #(* (% "green")
              (% "red")
              (% "blue")))
     (reduce +))

