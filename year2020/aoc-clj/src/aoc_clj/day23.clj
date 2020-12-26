(ns aoc-clj.day23)

(def test-input [3 8 9 1 2 5 4 6 7])
(def input [1 8 6 5 2 4 9 7 3])

(defn parse [v] (into {} (map vector v (rest (cycle v)))))
(defn unparse [m] (take (dec (count m)) (iterate m (m 1))))

(defn dest [cup max-cup picks]
  (cond
    (zero? cup) (recur max-cup max-cup picks)
    (picks cup) (recur (dec cup) max-cup picks)
    :else cup))

(defn solve [cup cups moves]
  (let [max-cup (apply max (keys cups))]
    (->> [cup (transient cups)]
         (iterate
           (fn [[c cs]]
             (let [p0 (cs c), p1 (cs p0), p2 (cs p1)
                   i (dest (dec c) max-cup #{p0 p1 p2})]
               [(cs p2)
                (assoc! cs c (cs p2), i p0, p2 (cs i))])))
         (#(nth % moves))
         second
         persistent!)))

(defn p1 [[cup :as cups]]
  (apply str (unparse (solve cup (parse cups) 100))))

(defn p2 [[cup :as cups]]
  (let [lo (inc (apply max cups))
        hi (inc 1e6)
        cups' (concat cups (range lo hi))]
    (apply * (take 2 (unparse (solve cup (parse cups') 1e7))))))

(comment
  (= "67384529" (p1 test-input))
  (= "45983627" (p1 input))
  (= 149245887792 (p2 test-input))
  (= 111080192688 (p2 input)))
