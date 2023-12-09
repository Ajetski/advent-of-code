(ns day08
  (:require [core :refer [get-puzzle-input]]))

(defn parse-map [lines]
  (->> lines
       (map #(re-find #"(.*) = \((.*), (.*)\)" %))
       (map rest)
       (map (fn [[a b c]] [a [b c]]))
       (into {})))

(defn get-lcm [x]
     (let
       [gcd (fn gcd [a b] (if (= 0 b) a (gcd b (mod a b))))
        lcm (fn lcm [a b] (/ (* a b) (gcd a b)))]
     (reduce lcm x)))

(let [[dir _ & lines] (get-puzzle-input 8)
      dir-idx (map #(condp = %
                      \L 0
                      \R 1)
                   dir)
      data (parse-map lines)]
  (loop [node "AAA"
         [dir-idx & drest] (cycle dir-idx)
         n 0]
    (if (= node "ZZZ")
      n
      (recur (get (data node) dir-idx)
             drest
             (inc n)))))

(let [[dir _ & lines] (get-puzzle-input 8)
      dir-idx (map #(condp = %
                      \L 0
                      \R 1)
                   dir)
      data (parse-map lines)
      starts (->> data
                  keys
                  (filter #(= (last %) \A)))]
  (->> starts
       (map #(loop [node %
               [dir-idx & drest] (cycle dir-idx)
               n 0]
          (if (= (last node) \Z)
            n
            (recur (get (data node) dir-idx)
                   drest
                   (inc n)))))
       (get-lcm)))

