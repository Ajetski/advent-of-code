(ns day23
  (:require
   [clojure.string :as str]
   [lonocloud.synthread.macros :refer :all]
   [core :refer [get-puzzle-input]]))

; (def input (get-puzzle-input 23))

(def input (str/split-lines "#.#####################
#.......#########...###
#######.#########.#.###
###.....#.>.>.###.#.###
###v#####.#v#.###.#.###
###.>...#.#.#.....#...#
###v###.#.#.#########.#
###...#.#.#.......#...#
#####.#.#.#######.#.###
#.....#.#.#.......#...#
#.#####.#.#.#########v#
#.#...#...#...###...>.#
#.#.#v#######v###.###v#
#...#.>.#...>.>.#.###.#
#####v#.#.###v#.#.###.#
#.....#...#...#.#.#...#
#.#########.###.#.#.###
#...###...#...#...#.###
###.###.#.###v#####v###
#...#...#.#.>.>.#.>.###
#.###.###.#.###.#.#v###
#.....###...###...#...#
#####################.#"))

(def start [0 1])
(def end [(dec (count input))
          (- (count (last input)) 2)])

(def char-map (->> input
                   (map-indexed (fn [row line]
                                  (map-indexed (fn [col c]
                                                 [[row col] c])
                                               line)))
                   (apply concat)
                   (into {})))

(def offsets [[0 1] [0 -1] [1 0] [-1 0]])

;; part 1
(->> (loop [q [{:loc start, :visited #{}}]
       ans #{}]
  (if (empty? q)
    ans
    (let [[{:keys [loc visited]}] q
          visited' (conj visited loc)
          [x y] loc]
      (if (= end loc)
        (recur (rest q) (conj ans (count visited)))
        (recur (concat (rest q)
                       (-> []
                           (>for [o offsets]
                                 (>let [[x' y' :as loc'] (mapv + loc o)]
                                       (>when (and (not (visited loc'))
                                                   (or (= \. (char-map loc'))
                                                       (and (= \> (char-map loc')) (< y y'))
                                                       (and (= \< (char-map loc')) (> y y'))
                                                       (and (= \v (char-map loc')) (< x x'))
                                                       (and (= \^ (char-map loc')) (> x x'))))
                                              (conj loc'))))
                           (>each (#(hash-map :loc % :visited visited')))))
               ans)))))
     (apply max))

;; part 2
(->> (loop [q [{:loc start, :visited #{}}]
       ans #{}]
  (if (empty? q)
    ans
    (let [[{:keys [loc visited]}] q
          visited' (conj visited loc)]
      (if (= end loc)
        (recur (rest q) (conj ans (count visited)))
        (recur (concat (rest q)
                       (-> []
                           (>for [o offsets]
                                 (>let [[x' y' :as loc'] (mapv + loc o)]
                                       (>when (and (not (visited loc'))
                                                   (#{ \. \> \< \v \^} (char-map loc')))
                                              (conj loc'))))
                           (>each (#(hash-map :loc % :visited visited')))))
               ans)))))
     (apply max))
