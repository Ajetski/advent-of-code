(ns day05
  (:require [input-manager]
            [clojure.string :as str]))

(def input (input-manager/get-input 2015 5))

(def bad #{"ab" "cd" "pq" "xy"})

;; part 1
(count (filter #(and (not (some (partial str/includes? %) bad))
                     (>= (count (filter #{\a \e \i \o \u} %)) 3)
                     (some (partial apply =) (concat (partition 2 %)
                                                     (partition 2 (rest %)))))
               input))
