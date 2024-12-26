(ns day24
  (:require
   [clojure.math :as math]
   [clojure.string :as s]
   [core :as c :refer :all]
   [input-manager :as i]))

(def sample "x00: 1
x01: 0
x02: 1
x03: 1
x04: 0
y00: 1
y01: 1
y02: 1
y03: 1
y04: 1

ntg XOR fgs -> mjb
y02 OR x01 -> tnw
kwq OR kpj -> z05
x00 OR x03 -> fst
tgd XOR rvg -> z01
vdt OR tnw -> bfw
bfw AND frj -> z10
ffh OR nrd -> bqk
y00 AND y03 -> djm
y03 OR y00 -> psh
bqk OR frj -> z08
tnw OR fst -> frj
gnj AND tgd -> z11
bfw XOR mjb -> z00
x03 OR x00 -> vdt
gnj AND wpb -> z02
x04 AND y00 -> kjc
djm OR pbm -> qhw
nrd AND vdt -> hwm
kjc AND fst -> rvg
y04 OR y02 -> fgs
y01 AND x02 -> pbm
ntg OR kjc -> kwq
psh XOR fgs -> tgd
qhw XOR tgd -> z09
pbm OR djm -> kpj
x03 XOR y03 -> ffh
x00 XOR y04 -> ntg
bfw OR bqk -> z06
nrd XOR fgs -> wpb
frj XOR qhw -> z04
bqk OR frj -> z07
y03 OR x01 -> nrd
hwm AND bqk -> z03
tgd XOR rvg -> z12
tnw OR pbm -> gnj")

(let [[a b] (c/split-on-double-newlines
             (i/get-input-raw 2024 24)
             ; sample
             ;
             )]
  (def registers (->> (s/split-lines a)
                      (map #(c/get-match-groups #"^(.*): (.*)$" %))
                      (map first)
                      (map vec)
                      (into {})
                      (#(update-vals % parse-long))))
  (def instrs (->> (s/split-lines b)
                   (map c/split-whitespace)
                   (map (fn [[a op b _ out]]
                          {:args #{a b}
                           :op op
                           :out out}))
                   (group-by :out)
                   (#(update-vals % first)))))

(defn get-var [prefix tape]
  (->> (filter (fn [[reg value]]
                 (s/starts-with? reg prefix))
               tape)
       sort
       reverse
       (map second)
       (s/join)))

(defn get-op-fn [op]
  (condp = op
    "AND" bit-and
    "OR" bit-or
    "XOR" bit-xor))

;; part 1
(->> (loop [tape registers
            q (vec instrs)]
       (if (empty? q)
         tape
         (let [[out curr :as instr] (first q)
               {:keys [args op]} curr
               operands (map (juxt identity tape) args)
               to-be-computed (filter #(nil? (second %)) operands)
               q' (subvec q 1)]
           (cond
             ;; already computed
             (tape out) (recur tape q')

             ;; needs dep
             (seq to-be-computed) (recur tape
                                         (-> q'
                                             (conj instr)))

             ;; ready to compute
             :else (recur (assoc tape out (apply (get-op-fn op)
                                                 (map second operands)))
                          q')))))

     (get-var "z")
     binary->long)

;; part 2
;; output of machine should be
(->> ["x" "y"]
     (map #(get-var % registers))
     (map binary->long)
     (apply +)
     long->binary)

;; find what gates need to be flipped such that part 1 outputs that num
