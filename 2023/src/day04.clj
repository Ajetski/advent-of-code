(ns ^{:doc "Day 4"
      :author "Adam Jeniski"}
 day04 (:require
        [clojure.math :as math]
        [clojure.set :refer [intersection]]
        [clojure.string :as string]
        [core :refer [get-puzzle-input]]))

(def lines (get-puzzle-input 4))

(defn parse-lines [lines]
  (->> lines
       (map #(rest (re-find #"^Card\s*\d+: ([\s\d]+) \| ([\s\d]+)" %)))
       (map (partial map #(->> (string/split % #" ")
                               (filter not-empty)
                               (map (fn [x] (Integer/parseInt x)))
                               (into #{}))))))

;; part 1
(->> (parse-lines lines)
     (map (partial apply intersection))
     (map count)
     (map #(math/pow 2 (dec %)))
     (map int)
     (reduce +))

;; part 2
(loop [acc 0
       data (->> (parse-lines lines)
                 (map #(vector 1 (count (apply intersection %))))
                 (into []))]
  (if (not-empty data)
    (let [[card-cnt num-matches] (first data)]
      (recur (+ acc card-cnt)
             (concat (->> (rest data)
                          (take num-matches)
                          (map #(update % 0 + card-cnt)))
                     (drop (inc num-matches) data))))
    acc))

