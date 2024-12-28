(ns day23
  (:require
   [clojure.math :as math]
   [clojure.math.combinatorics :as combo]
   [clojure.string :as s]
   [core :as c :refer :all]
   [input-manager :as i]))

(def edges (->> (i/get-input 2024 23)
                (map #(c/get-match-groups #"^(.*)-(.*)$" %))
                (map first)
                (map (partial into #{}))
                set))

(def nodes (->> edges
                (map seq)
                flatten
                set))

;; part 1
(def part-1-ans
  (future
    (->> (combo/combinations edges 2)
         (mmap vec)
         (map flatten)
         (map set)
         (filter #(= (count %) 3))
         (map seq)
         (filter (partial some #(.startsWith % "t")))
         (filter (fn [[a b _]] (edges #{a b})))
         (filter (fn [[_ b c]] (edges #{b c})))
         (filter (fn [[a _ c]] (edges #{a c})))
         (map set)
         set
         count)))
(future
  (println "processing part 1...")
  (println "part 1 answer:" @part-1-ans))

(comment
  (realized? part-1-ans)
  (deref part-1-ans 0 "still processing")
  @part-1-ans)

