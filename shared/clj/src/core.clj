(ns core
  (:require
   [clojure.string :as str]))

;; string/regex stuff

(defn split-on-comma [s]
  (str/split s #","))

(defn split-whitespace [s]
  (str/split s #"\s+"))

(defn split-on-double-newlines [s]
  (str/split s #"\n\n"))

(defn get-match-groups [regex s]
  (->> s (re-seq regex) (map rest)))

(defn re-pos [re s]
  (loop [m (re-matcher re s)
         res {}]
    (if (.find m)
      (recur m (assoc res (.start m) (.group m)))
      res)))

;; general utils

(defn dbg
  "prints a value and returns it.
  useful for inserting into ->, ->>, and comp

  (->> x
       some-fn
       dbg
       (dbg \"optional tag/prefix:\")
       some-fn-2)"
  ([x]
   (println x)
   x)
  ([prefix x]
   (println prefix x)
   x))

(defn log
  "faster than dbg, for those really tricky graph problems
  or just ya know, writing to your diary"
  ([msg]
   (spit "logs.txt" msg :append true))
  ([file msg]
   (spit file msg :append true)))

(defn compose
  "just comp, but in the \"right\" order (left-to-right evaluation of fns)"
  ;; several inlined arrities for optimization
  ([f1]
   f1)
  ([f1 f2]
   (comp f2 f1))
  ([f1 f2 f3]
   (comp f3 f2 f1))
  ([f1 f2 f3 f4 & fs]
   (comp (apply comp (reverse fs)) f4 f3 f2 f1)))

;; alter collections

(defn get-coords [list-of-lists]
  (for [row (range (count list-of-lists))
        col (range (count (get list-of-lists row)))]
    [row col]))

(defn map-by-coords [arr-2d]
  (->> arr-2d
       get-coords
       (map (juxt identity #(get (get arr-2d (first %)) (second %))))
       (into {})))

(defn map-to-coords [arr-2d]
  (->> arr-2d
       get-coords
       (map (juxt #(get (get arr-2d (first %)) (second %)) identity))
       (into {})))

(defn insert-at-idx [coll idx el]
  (concat (take idx coll)
          (list el)
          (drop idx coll)))

(defn n-map
  "(map map map... f coll) with n maps times"
  [n f & colls]
  (loop [n n
         mapping-f f]
    (cond
      (<= n 0) (apply mapping-f colls)
      (= 1 n) (apply map mapping-f colls)
      :else (recur (dec n)
                   (partial map mapping-f)))))

(defn n-mapv
  "(mapv mapv mapv... f coll) with n maps times"
  [n f & colls]
  (loop [n n
         mapping-f f]
    (if (= 1 n)
      (apply mapv mapping-f colls)
      (recur (dec n)
             (partial mapv mapping-f)))))

(defn mmap
  "map map f coll"
  [f & colls]
  (apply map (partial map f) colls))

(defn mmapv
  "mapv mapv f coll"
  [f & colls]
  (apply mapv (partial mapv f) colls))

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

(defn partition-contiguous-nums [sorted-nums]
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

;; Math things

(defn square [n] (* n n))
(defn mean [a] (/ (reduce + a) (count a)))
(defn standard-deviation [a]
  (let [mn (mean a)]
    (Math/sqrt
     (/ (reduce #(+ %1 (square (- %2 mn))) 0 a)
        (dec (count a))))))

(defn cartesian-product [a b]
  (partition (count b)
             (for [el1 a
                   el2 b]
               (* el1 el2))))

;; conversions

(defn binary->long [binary-str]
  (Long/parseLong binary-str 2))

(defn long->binary [value]
  (Long/toString value 2))

(def arrow-char->dir {\> :right
                      \v :down
                      \< :left
                      \^ :up})

(defn bool->binary-int
  "converts bool to integral binary representation (0 or 1)"
  [condition]
  (if condition 1 0))

;; 👻 macros 👻

(defmacro pfor
  "pairwise for, like clojure.core/for except NOT cartesian.
  linear result over arrs"
  [bindings & body]
  (let [vars (take-nth 2 bindings)
        forms (take-nth 2 (rest bindings))]
    `(map (fn [~@vars] ~@body) ~@forms)))

(comment
  (def ^:private a [1 2 3])
  (def ^:private b [3 2 1])
  (pfor [x a
         y b
         z [0 1 2]]
        (+ x y z)))
