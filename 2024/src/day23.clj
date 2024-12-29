(ns day23
  (:require
   [clojure.math.combinatorics :as combo]
   [clojure.set :as set]
   [clojure.string :as str]
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

;; part 1, find interconncceted combos of 3 with nodes with at least one like #"t.*"
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

(comment

  (future
    (println "processing part 1...")
    (println "part 1 answer:" @part-1-ans))

  (realized? part-1-ans)
  (deref part-1-ans 0 "still processing")
  @part-1-ans)

(defn make-graph [nodes edges]
  (reduce (fn [graph el]
            (assoc graph el (->> (filter #(contains? % el) edges)
                                 (map #(disj % el))
                                 (map first)
                                 (set))))
          {}
          nodes))

(def graph (make-graph nodes edges))

;; part 2, find largest interconnected subgraph
(->> (for [guess (->> graph
                      (map reverse)
                      (map (partial apply conj)))
           part (combo/combinations (seq guess) 1)]
       (reduce set/intersection
               (map #(apply sorted-set (conj (graph %) %))
                    (apply disj guess part))))
     (filter (compose count #(> % 1)))
     (map (partial str/join ","))
     (apply max-key (compose (partial filter #(= \, %)) count)))
