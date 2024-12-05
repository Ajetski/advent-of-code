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

(defn get-coords
  "returns a lazy seq representing list of list x, y tuples"
  [list-of-lists]
  (->> list-of-lists count range
       (map #(->> % (get list-of-lists) count range))
       (map-indexed (fn [row cols]
                      (map #(list row %) cols)))
       (mapcat identity)))

(defn bool->binary [condition]
  (if condition 1 0))
