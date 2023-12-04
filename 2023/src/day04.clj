(ns ^{:doc "Day 4"
      :author "Adam Jeniski"}
 day04 (:require
        [clojure.math :as math]
        [clojure.set :refer [intersection]]
        [clojure.string :as string]
        [core :refer [get-puzzle-input]]))

(def lines (get-puzzle-input 4))

(defn parse-nums [nums]
  (->> (string/split nums #" ")
       (filter not-empty)
       (map #(Integer/parseInt %))
       (into #{})))

;; part 1
(->> lines
     (map #(re-find #"^Card\s*\d+: ([\s\d]+) \| ([\s\d]+)" %))
     (map rest)
     (map (partial map parse-nums))
     (map (partial apply intersection))
     (map count)
     (map #(math/pow 2 (dec %)))
     (map int)
     (reduce +))

;; part 2
(loop [acc 0
       data (->> lines
                 (map #(re-find #"^Card\s*\d+: ([\s\d]+) \| ([\s\d]+)" %))
                 (map rest)
                 (map (fn [[win actual]]
                        [1 (count (intersection (parse-nums win)
                                                (parse-nums actual)))]))
                 (into []))]
  (if (not-empty data)
    (let [cnt-to-add (second (first data))
          curr-cnt (ffirst data)]

      (recur
       (+ acc curr-cnt)
       (concat (->> data
                    rest
                    (take cnt-to-add)
                    (map #(update % 0 + curr-cnt)))
               (drop (inc cnt-to-add) data))))
    acc))

