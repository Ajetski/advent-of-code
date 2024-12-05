(ns core
  (:require
   [clojure.string :as str]))

(defn compose [& fs]
  (apply comp (reverse fs)))

(defn split-whitespace [s]
  (str/split s #"\s+"))

(defn get-match-groups [regex s]
  (->> s (re-seq regex) (map rest)))

(defn re-pos [re s]
  (loop [m (re-matcher re s)
         res {}]
    (if (.find m)
      (recur m (assoc res (.start m) (.group m)))
      res)))

(defn dbg [x]
  (println x)
  x)

(defn get-coords [list-of-lists]
  (for [row (range (count list-of-lists))
        col (range (count (get list-of-lists row)))]
    [row col]))

(defn insert-vec [v idx el]
  (into [] (concat (take idx v)
                   (list el)
                   (drop idx v))))

(defn bool->binary [condition]
  (if condition 1 0))
