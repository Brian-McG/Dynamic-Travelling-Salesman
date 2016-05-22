# Dynamic-Travelling-Salesman
## Author
Brian Mc George - MCGBRI004
## Result Comparison
Jethro Muller - MULJET001

## Prerequisites
  - Java 8 (Java 7 **should** work too)

## To run (in terminal)
  - Change directory to the src folder
  - javac TSP.java
  - java TSP 100

## Brian's Result Summary
Average best fitness: 3767
Best fitness: 3193

## Jethro's Result Summary
Average best fitness: 4148
Best fitness: 3383

## Normality Tests
Two different applications were used when determining normality, the online KS test provided in the slides (http://www.physics.csbsju.edu/cgi-bin/stats/KS-test.n.plot) and SPSS 23 (since I will likely use it for various statistical tasks in my Honours project)

### Online KS test
#### Jethro
KS finds the data is consistent with a normal distribution: P= 0.73 where the normal distribution has mean= 4138. and sdev= 264.1
#### Brian
KS finds the data is consistent with a normal distribution: P= 0.52 where the normal distribution has mean= 3753. and sdev= 193.0

### SPSS
SPSS runs both the KS test and Shapiro-Wilk tests. The documentation states that KS tests are best for large datasets while Shapiro-Wilk is better for datasets < 2000.
The hypotheses for the tests are:
Ho: The distribution of the data is normal
Ha: The distribution of the data is not normal

We will reject Ho if the p-value < 0.05

Tests of Normality
|------|-----------------------------|---------------------|
|      |Kolmogorov-Smirnova (a)      |    Shapiro-Wilk     |
|      |-------------------|---|-----|------------|---|----|
|      |Statistic          |df |Sig. |Statistic   |df |Sig.|
|------|-------------------|---|-----|------------|---|----|
|Jethro|.055               |100|.200*|.988        |100|.503|
|------|-------------------|---|-----|------------|---|----|
|Brian |.043               |100|.200*|.984        |100|.288|
|----------------------------------------------------------|
 * This is a lower bound of the true significance.
 a Lilliefors Significance Correction

Since the Sig. (p-value) for each test is > 0.05 we accept Ho and assume the distribution of the datasets are normal.

## Two sampled t-tests
Since the datasets passed the normality tests, we can run two sampled t-tests to determine if there is a statistically significant difference between the mean of the two datasets.
Two different applications were used when running the t-tests, the t-test provided in the slides (http://www.physics.csbsju.edu/stats/t-test_bulk_form.html) and SPSS 23
The hypotheses for the tests are:
Ho: The mean cost between the two datasets are the same
Ha: The mean cost between the two datasets are different

### Online t-test
t= 13.3
sdev= 203.
degrees of freedom =198 The probability of this result, assuming the null hypothesis, is less than .0001

### SPSS
Independent Samples Test
|--------------------------------|--------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------|
|                                |Levene's Test for Equality of Variances     |t-test for Equality of Means                                                                                                                 |
|                                |---------------------------------------|----|----------------------------|-------|---------------|---------------|---------------------|--------------------------------------------------|
|                                |F                                      |Sig.|t                           |df     |Sig. (2-tailed)|Mean Difference|Std. Error Difference|95% Confidence Interval of the Difference         |
|                                |                                       |    |                            |       |               |               |                     |-----------------------------------------|--------|
|                                |                                       |    |                            |       |               |               |                     |Lower                                    |Upper   |
|----|---------------------------|---------------------------------------|----|----------------------------|-------|---------------|---------------|---------------------|-----------------------------------------|--------|
|Cost|Equal variances assumed    |8.622                                  |.004|13.258                      |198    |.000           |380.4800       |28.6976              |323.8879                                 |437.0721|
|    |---------------------------|---------------------------------------|----|----------------------------|-------|---------------|---------------|---------------------|-----------------------------------------|--------|
|    |Equal variances not assumed|                                       |    |13.258                      |180.034|.000           |380.4800       |28.6976              |323.8531                                 |437.1069|
|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|

Since the Sig. (p-value) is < 0.05 for both SPSS and the online t-test, we reject Ho and conclude that Brian's solution offers a significant reduction in cost over Jethro's solution.

## Discussion
My approach only uses mutation where Jethro's uses crossover too. I do not feel crossover will be that beneficial for this problem space. Additionally, Jethro mutates the top 10 chromosomes where I focus exclusively on mutating the best chromosome.
However, the likely major difference is I mutate a mutate if it is deemed better than the current best parent (confirmed to be allowed by Dr. Nitschke).
In addition, I change (at most) 4 chromosomes of the population in a generation. This is because I know the landscape is dynamic so what is good now may not be good for another landscape.
I therefore try keep the majority of the other population around so that we can find a very good local optima for each landscape instead of lots of optima for a specific landscape which may change in the next generation.
My approach also ensures that we evaluate at most 100 mutates in a generation and ensures that the population never exceeds 100 (even temporarily).
