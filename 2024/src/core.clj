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

(defn map-by-coords [arr-2d]
  (->> arr-2d
       get-coords
       (map (juxt identity #(get (get arr-2d (first %)) (second %))))
       (into {})))

(defn insert-at-idx [coll idx el]
  (concat (take idx coll)
          (list el)
          (drop idx coll)))

(defn bool->binary [condition]
  (if condition 1 0))

(defn mmap
  "map map f coll"
  [f & colls]
  (apply map (partial map f) colls))

(defn mmapv
  "mapv mapv f coll"
  [f & colls]
  (apply mapv (partial mapv f) colls))

(defn mmmap
  "map map map f coll"
  [f & colls]
  (apply map (partial map (partial map f)) colls))

(defn mmmapv
  "mapv mapv mapv f coll"
  [f & colls]
  (apply mapv (partial mapv (partial mapv f)) colls))

(defn partition-by-counts [counts coll]
  (->> counts
       (reduce (fn [[acc coll] c]
                 (let [[a b] (split-at c coll)]
                   [(conj acc a) b]))
               [[] coll])
       first))

(defn update-last [v f & args]
  (let [idx (dec (count v))]
    (apply update v idx f args)))

(defn partition-by-range-gap [sorted-nums]
  (:acc (reduce (fn [{acc :acc
                      a :last}
                     el]
                  (if (= el (inc a))
                    {:acc (update-last acc conj el)
                     :last el}
                    {:acc (conj acc [el])
                     :last el}))
                {:acc [[(first sorted-nums)]]
                 :last (first sorted-nums)}
                (rest sorted-nums))))

