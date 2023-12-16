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
  [& ls] (let [r (reverse ls)]
           `(comp ~@r)))

(defmacro static-fn
  "wraps java static methods in a lambda so they can be passed as function objects"
  [f] `#(~f %))

(defn mapvf
  "returns a function that does mapv over f when called with a coll"
  [f] #(mapv f %))

(defn reducef
  "returns a function that reduces over f when called with a coll"
  ([f] #(reduce f %))
  ([f init] #(reduce f init %)))

(defmacro mfn
  "like fn but memoizes return values, including recursive calls"
  [name arglist & body]
  `(let [dp# (atom {})
         f# (fn ~name ~arglist
              (or (@dp# [~@arglist])
                  (let [res# ~@body]
                    (swap! dp# assoc [~@arglist] res#)
                    res#)))]
     f#))

(defmacro defmfn
  "like defn but for a memoized fn, see ajet.core/mfn"
  [name & args]
  `(def ~name (mfn ~name ~@args)))

(comment
  (map (static-fn Long/parseLong) ["123" "456"])
  input-cache

  (defmfn fib [x]
    (if (< x 2)
      x
      (+ (fib (dec x))
         (fib (- x 2)))))
  (fib 200N))
