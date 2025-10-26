(ns day16
  (:require
   [input-manager :refer [get-input]]
   [core :as c]
   [clojure.data.priority-map :refer [priority-map]]))

(def maze
  #_(c/split-whitespace "###############
#.......#....E#
#.#.###.#.###.#
#.....#.#...#.#
#.###.#####.#.#
#.#.#.......#.#
#.#.#####.###.#
#...........#.#
###.#.#####.#.#
#...#.....#.#.#
#.#.#.###.#.#.#
#.....#...#.#.#
#.###.#.#.#.#.#
#S..#.....#...#
###############")
  (get-input 2024 16))
(def grid (->> maze
               (map-indexed (fn [row-idx row]
                              (map-indexed (fn [col-idx c]
                                             [[row-idx col-idx] c])
                                           row)))
               (mapcat identity)
               (into {})))
(defn find-char [c]
  (->> grid
       (filter (comp #{c} second))
       ffirst))
(def start (find-char \S))
(def end (find-char \E))

(defn rotate-clockwise [dir]
  (condp = dir
    :right :down
    :down :left
    :left :up
    :up :right))
(defn rotate-counterclockwise [dir]
  (condp = dir
    :down :right
    :left :down
    :up :left
    :right :up))
(defn direction-offset [dir]
  (condp = dir
    :up [-1 0]
    :right [0 1]
    :down [1 0]
    :left [0 -1]))

(defn find-search-path [[start start-dir] goal]
  (loop [[[[pos dir] cost] & _ :as whole] (priority-map [start start-dir] 0)
         visited #{[start start-dir]}
         shortest {start 0}
         n 0]
    (if (= pos goal)
      shortest
      (let [searches (->> [[[(mapv + pos (direction-offset dir)) dir] (inc cost)]
                           [[pos (rotate-clockwise dir)] (+ 1000 cost)]
                           [[pos (rotate-counterclockwise dir)] (+ 1000 cost)]]
                          (filter (comp not visited first))
                          (filter (comp grid ffirst))
                          (filter (comp not #{\#} grid ffirst)))
            searches-no-costs (mapv (comp first pop) searches)
            seaches-no-dirs (mapv (juxt ffirst last) searches)]
        (recur (into (pop whole) searches)
               (into visited searches-no-costs)
               (into (into {} seaches-no-dirs) shortest)
               (inc n))))))

(def searched (find-search-path [start :right] end))

;; part 1 solution
(searched end)

(def max-idx (->> (keys grid)
                  (map first)
                  (apply max)))
(def updated-grid (->> grid
                       (map (fn [[k :as entry]] (if (and (contains? searched k) (#{\S \E \.} (grid k)))
                                                  [k \O]
                                                  entry)))
                       (into {})))
(->> (range 0 max-idx)
     (map (fn [row] (map #(vector row %) (range 0 max-idx))))
     (map (partial map updated-grid))
     (map (partial apply str))
     (interpose "\n")
     (apply str)
     print)


