(ns day17
  (:require
   [clojure.string :refer [split-lines]]
   [core :refer [get-puzzle-input log]]))

(comment
  (def input (get-puzzle-input 17))
  (def input (split-lines (slurp "input"))))

(def heat-map
  (->> input
       (map-indexed (fn [row line]
                      (map-indexed (fn [col c]
                                     [[row col] (parse-long (str c))])
                                   line)))
       (apply concat)
       (into {})))

(def N (count input))

(defn next-move-cnt [md dir]
  #(if (= md dir) (inc %) 1))

(defn next-nodes [{md :move-dir :as n}]
  (->> [(-> (update n :loc update 1 inc)
            (update :move-cnt (next-move-cnt md :right))
            (assoc :move-dir :right))
        (-> (update n :loc update 1 dec)
            (update :move-cnt (next-move-cnt md :left))
            (assoc :move-dir :left))
        (-> (update n :loc update 0 inc)
            (update :move-cnt (next-move-cnt md :down))
            (assoc :move-dir :down))
        (-> (update n :loc update 0 dec)
            (update :move-cnt (next-move-cnt md :up))
            (assoc :move-dir :up))]
       (filter (fn [{[x y] :loc}]
                 (and (< -1 x N)
                      (< -1 y N))))
       (filter (fn [{mc :move-cnt}]
                 (<= mc 3)))
       (filter (fn [{md' :move-dir}]
                 (not (or (and (= md :up)    (= md' :down))
                          (and (= md :down)  (= md' :up))
                          (and (= md :right) (= md' :left))
                          (and (= md :left)  (= md' :right))))))
       (map (fn [{loc :loc, :as n}]
              (update n :heat-loss #(+ % (heat-map loc)))))))

(def priority (juxt (comp #(* % -1) :heat-loss)
                    :loc))
(defn compare-priority [a b]
  (compare (priority b) (priority a)))


;; part 1
(loop [pq (sorted-set-by compare-priority {:loc [0 0]
                                           :move-cnt 0
                                           :move-dir nil :heat-loss 0})]
  (when (not-empty pq)
    (let [node (first pq)]
      (if (= (:loc node)
             [(dec N) (dec N)])
        node
        (recur (reduce conj (disj pq node) (next-nodes node)))))))

