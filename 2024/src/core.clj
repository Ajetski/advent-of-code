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

(defn insert-at-idx [coll idx el]
  (concat (take idx coll)
          (list el)
          (drop idx coll)))

(defn bool->binary [condition]
  (if condition 1 0))

(defn mmap
  "map map f coll"
  [f coll]
  (map (partial map f) coll))

(defn mmapv
  "mapv mapv f coll"
  [f coll]
  (mapv (partial mapv f) coll))

(defn mmmap
  "map map map f coll"
  [f coll]
  (map (partial map (partial map f)) coll))

(defn mmmapv
  "mapv mapv mapv f coll"
  [f coll]
  (mapv (partial mapv (partial mapv f)) coll))

(defn partition-by-counts [counts coll]
  (->> counts
       (reduce (fn [[acc coll] c]
                 (let [[a b] (split-at c coll)]
                   [(conj acc a) b]))
               [[] coll])
       first))
