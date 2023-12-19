(ns core
  (:require [clj-http.client :as client]
            [clojure.string :as string]))

(def input-cache (atom {}))
(def cookie (str "session=" (slurp "session")))

(defn get-puzzle-input
  "retrieves and caches aoc input for 2023 given a day"
  [day]
  (or (@input-cache day)
      (swap! input-cache assoc day
             (-> (str "https://adventofcode.com/2023/day/" day "/input")
                 (client/get {:throw-entire-message? true
                              :headers {"Cookie" cookie}})
                 :body
                 string/split-lines))))

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

(defmacro comp>
  "comp, but fn-args are applied from left to right"
  [& ls] (let [r# (reverse ls)]
           `(comp ~@r#)))

(defmacro s-fn
  "wraps java static methods in a lambda so they can be passed as function objects"
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
                  (let [res# ~@body]
                    (swap! dp# assoc [~@arglist] res#)
                    res#)))]
     f#))

(defmacro defn-m
  "like defn but for a memoized fn, see ajet.core/fn-m"
  [name & args]
  `(def ~name (fn-m ~name ~@args)))

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
  [& args]
  (->> (zipmap (drop-last args)
               (last args))
       (mapv #((first %) (second %)))))

(comment
  (map (s-fn Long/parseLong) ["123" "456"])
  input-cache

  ((mapvf #(* 2 %)) [1 2])
  ((reducef + 0) [1 2])

  ((comp> #(* % 3)
          #(+ % 2)
          #(* % 4))
   5)

  ;; is always 1, comp> is comp but reversed (to be the proper way round lol)
  (->> 20
       ((juxt (comp> #(* % 3) #(+ % 2) #(* % 4))
              (comp  #(* % 4) #(+ % 2) #(* % 3))))
       distinct
       count)


  (apply-each #(* % 2) #(+ % 5) 5 10)
  (apply-each-v #(* % 2) #(+ % 5) [5 10])

  (log (+ 5 5)
       (- 2 1))

  (defn-m fib [x]
    (if (< x 2)
      x
      (+ (fib (- x 2))
         (fib (dec x)))))

  ;; 2000+ digit number generated in <16ms (leveraging polymorphism and big-int)
  ;; using a seemingly naive O(n!) implementation (leveraging defn-m, autocaching)
  (time (fib 10000N)))
