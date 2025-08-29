(ns day13
  (:require
   [core :as c]
   [input-manager :refer [get-input-raw]]))

(def input
  (->> (get-input-raw 2024 13)
       c/split-on-double-newlines
       (map #(c/get-match-groups #"(\d+)" %))
       (c/mmap first)
       (c/mmap parse-long)
       (map #(map vector [:ax :ay :bx :by :px :py] %))
       (map #(into {} %))))

(defn get-cost [{:keys [ax ay bx by px py]}]
  ;; systems of equations. had to get out a pen and paper to work out a and b ;P
  ;; px = ax * a + bx * b
  ;; py = ay * a + by * b
  (let [b (/ (- (* ax py)
                (* ay px))
             (- (* ax by)
                (* bx ay)))
        a (/ (- px (* b bx))
             ax)]
    (when (= 0.0
             (mod a 1.0)
             (mod b 1.0))
      (+ (* 3 a) b))))

(comment
;; part 1
  (->> input
       (map get-cost)
       (filter identity)
       (filter identity)
       (reduce +)
       ;
       )

  (def offset 10000000000000)
  (->> input

       (map #(update % :px + offset))
       (map #(update % :py + offset))
       (map get-cost)
       (filter identity)
       (reduce +)))
