(ns day13
  (:require
   [core :as c]
   [input-manager :refer [get-input-raw]]))

(def input
  (->> (get-input-raw 13)
       c/split-on-double-newlines
       (map #(c/get-match-groups #"(\d+)" %))
       (c/mmap first)
       (c/mmap parse-long)
       (map #(map vector [:ax :ay :bx :by :px :py] %))
       (map #(into {} %))
       ;
       ))

(defn push [m k vs]
  (if (empty? vs)
    m
    (update m k #(if (nil? %1)
                   (set vs)
                   (into %1 %2))
            vs)))

(defn get-cost [{:keys [ax ay bx by px py]}]
  (loop [costs (sorted-map 0 #{[0 0]})
         i 0]
    (let [[cost locs] (first costs)
          costs' (dissoc costs cost)]
      (if (or (nil? cost)
              (> i 1000)
              (some (fn [[x y]]
                      (and (= x px)
                           (= y py)))
                    locs))
        costs
        (recur (-> costs'
                   (push (+ cost 3)
                         (->> locs
                              (map #(-> %
                                        (update 0 + ax)
                                        (update 1 + ay)))
                              (filter (fn [[x y]]
                                        (and (<= x px)
                                             (<= y py))))))
                   (push (+ cost 1)
                         (->> locs
                              (map #(-> %
                                        (update 0 + bx)
                                        (update 1 + by)))
                              (filter (fn [[x y]]
                                        (and (<= x px)
                                             (<= y py)))))))
               (inc i))))))

;; part 1
(->> input
     (map get-cost)
     (filter identity)
     (reduce +))

