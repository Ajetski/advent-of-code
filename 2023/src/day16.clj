(ns day16 (:require [core :refer [get-puzzle-input]]))

(def input (get-puzzle-input 16))
(def char-map (->> input
                   (map-indexed (fn [row line]
                                  (map-indexed (fn [col c]
                                                 [[row col] c])
                                               line)))
                   (apply concat)
                   (into {})))
(def N (count input))

(defn next-loc [{dir   :dir
                 [r c] :loc
                 :as   arg}]
  (let [[r' c' :as ans] (condp = dir
                          :left  [r (dec c)]
                          :right [r (inc c)]
                          :up    [(dec r) c]
                          :down  [(inc r) c])]
    (when (and (< -1 r' N)
               (< -1 c' N))
      (assoc arg :loc ans))))

(defn apply-mirrors [{dir   :dir
                      loc   :loc
                      :as   arg}]
  (cond (and (#{:left :right} dir)
             (= (char-map loc) \|))
        (list (assoc arg :dir :up) (assoc arg :dir :down))

        (and (#{:up :down} dir)
             (= (char-map loc) \-))
        (list (assoc arg :dir :left) (assoc arg :dir :right))

        (= (char-map loc) \\)
        (list (assoc arg :dir (condp = dir
                                :left :up
                                :right :down
                                :up :left
                                :down :right)))

        (= (char-map loc) \/)
        (list (assoc arg :dir (condp = dir
                                :left :down
                                :right :up
                                :up :right
                                :down :left)))

        :else (list arg)))

(defn measure-energy [start]
  (->> (loop [beams [start]
              visited #{}]
         (let [beams' (->> (keep next-loc beams)
                           (map apply-mirrors)
                           (apply concat)
                           (filter (complement visited)))]
           (if (not-empty beams')
             (recur beams'
                    (apply conj visited beams'))
             visited)))
       (map :loc)
       distinct
       count))

;; part 1
(measure-energy {:dir :right
                 :loc [0 -1]})

;; part 2
(->> [(for [c (range N)] {:loc [N c],  :dir :up})
      (for [c (range N)] {:loc [-1 c], :dir :down})
      (for [r (range N)] {:loc [r -1], :dir :right})
      (for [r (range N)] {:loc [r N],  :dir :left})]
     (apply concat)
     (pmap measure-energy)
     (apply max))
