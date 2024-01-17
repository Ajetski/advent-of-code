(ns core
  (:require [clj-http.client :as client]
            [clojure.string :as string]))

(def cookie (str "session=" (slurp "session")))

(defn get-puzzle-input
  "retrieves and caches aoc input for 2023 given a day"
  [day]
  (let [file (java.io.File. (str "inputs/day" day ".txt"))]
    (or (when (.exists file)
          (string/split-lines (slurp file)))
        (let [data (-> (str "https://adventofcode.com/2023/day/" day "/input")
                       (client/get {:throw-entire-message? true
                                    :headers {"Cookie" cookie}})
                       :body)]
          (spit file data)
          (string/split-lines data)))))

(defn re-seq-pos
  "re-seq that produces a seq of {:start :end :group}"
  [pattern string]
  (let [m (re-matcher pattern string)]
    ((fn step []
       (when (. m find)
         (cons {:start (. m start) :end (. m end) :group (. m group)}
               (lazy-seq (step))))))))

(defn split-spaces [s]
  (string/split s #" "))

(defmacro let-dbg [arg-list & body]
  (let [pairs (partition 2 arg-list)
        definitions (->> pairs
                         (map (fn [[a b]]
                                `[~a (let [temp# ~b]
                                       (println '~a "=" temp#)
                                       temp#)]))
                         (apply concat))]
    `(let [~@definitions]
       ~@body)))

(defmacro comp>
  "generates a closure that composes inputs with clojure.core/-> semantics"
  [& forms]
  `(fn [v#] (-> v# ~@forms)))

(defmacro comp>>
  "generates a closure that composes inputs with clojure.core/->> semantics"
  [& forms]
  `(fn [v#] (->> v# ~@forms)))

(defmacro w-fn
  "wraps s-expr such as java static methods or macro calls
  in a lambda so they can be passed as function objects"
  [f] `(fn [v#] (~f v#)))

(defn mapvf
  "returns a function that does mapv over f when called with a coll"
  [f] #(mapv f %))

(defn reducef
  "returns a function that reduces over f when called with a coll"
  ([f] #(reduce f %))
  ([f init] #(reduce f init %)))

(defmacro fn-m
  "like fn but memoizes return values, including recursive calls"
  [name arglist & body]
  `(let [dp# (atom {})
         f# (fn ~name ~arglist
              (or (@dp# [~@arglist])
                  (let [res# (do ~@body)]
                    (swap! dp# assoc [~@arglist] res#)
                    res#)))]
     f#))

(defmacro defn-m
  "like defn but for a memoized fn, see ajet.core/fn-m"
  [name arglist & body]
  `(def ~name (fn-m ~name ~arglist ~@body)))

(defmacro log [& body]
  (let [exprs# (map (fn [e#]
                      `(let [e-res# ~e#]
                         (println "expr:" '~e#)
                         (println "result: " e-res#)
                         e-res#)) body)]
    `(do ~@exprs#
         nil)))

(defn apply-each
  "[& fs vs]
  fs is a list of funcitons of length N
  vs is a list of values of length N

  this function returns a vector where each value is the result of applying
  an f from fs to the corresponding v from vs"
  [& args]
  ;; TODO: add spec or manual validation for args
  (let [n (/ (count args) 2)]
    (->> (zipmap (take n args)
                 (take-last n args))
         (mapv #((first %) (second %))))))

(defn apply-each-v
  "[& fs vs]
  fs is a list of funcitons of length N
  v is a vector of values of length N

  this function returns a vector where each value is the result of applying
  an f from fs to the corresponding value from v"
  ;; TODO: add spec or manual validation for args
  [& args]
  (->> (zipmap (drop-last args)
               (last args))
       (mapv #((first %) (second %)))))

(defmacro assert-all [& assertions]
  `(do ~@(map (fn [assertion]
                `(assert ~assertion))
              assertions)))

(defn partial< [f & args1]
  (fn [& args2] (apply f (concat args2 args1))))

(comment

  (let-dbg [a 1
            b (inc a)]
           (+ a b))

  (defn abc [a b]
    (+ a b))

  (assert (= 1
             (inc 0)
             (dec 2)
             (+ (/ 1 2)
                (/ 1 2))))
  (assert
   (= ((comp> (+ 1) (/ 2) (abc 1) (inc) ;; currying / partial fn applicaiton
              inc ;; named fn
              #(+ 1 %) (fn [v] (+ v 1))) ;; using lambdas
       5)
      8))
  (assert (= ((comp> (- 5) (- 2))
              10)
             3))
  (assert (= ((comp>> (- 5) (- 2))
              10)
             7))

  (map (w-fn Long/parseLong) ["123" "456"])
  input-cache

  ((mapvf #(* 2 %)) [1 2])
  ((reducef + 0) [1 2])

  (let [v 20]
    (assert (= ((comp> #(* % 3) #(+ % 2) #(* % 4))
                v)
               ((comp  #(* % 4) #(+ % 2) #(* % 3))
                v))))

  (assert-all
   (=
    (apply-each #(* % 2) #(+ % 5) 1 2)
    (apply-each-v #(* % 2) #(+ % 5) [1 2])
    [2 7])
   (=
    (apply-each #(* % 2) #(+ % 5) 5 10)
    (apply-each-v #(* % 2) #(+ % 5) [5 10])
    [10 15]))

  (log (+ 5 5)
       (- 2 1))

  (defn-m fib [x]
    (if (< x 2)
      x
      (+ (fib (- x 2))
         (fib (dec x)))))

  (map (w-fn log) [1 2])

  ;; 2000+ digit number generated in <16ms (leveraging polymorphism and big-int)
  ;; using a seemingly naive O(n!) implementation (leveraging defn-m, autocaching)
  (time (fib 10000N))

  ;
  )
