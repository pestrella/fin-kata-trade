(ns trade-clj.core-test
  (:require [clojure.test :refer :all]
            [trade-clj.core :refer :all])
  (:import fin.trade.Trade))

(deftest no-price
  (testing "no known price"
    (is (thrown? RuntimeException
                 (. (trade-analyser) latestPrice "product_1")))))

(deftest zero-average
  (testing "zero average"
    (is (= (. (trade-analyser) averageVolume "product_1") 0))))

(deftest average
  (testing "average"
    (let [analyser (trade-analyser)]
      (doseq [trade [(Trade. "product_1" 3.5 (int 20000) (System/currentTimeMillis))
                     (Trade. "product_1" 4.2 (int 15000) (System/currentTimeMillis))]]
        (. analyser update trade))
      (is (= (. analyser averageVolume "product_1")
             17500)))))
