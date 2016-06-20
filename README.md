# Dynamic-Travelling-Salesman
#### Author
Brian Mc George - MCGBRI004
#### Result Comparison
Jethro Muller - MULJET001

### Prerequisites
  - Java 8 (if you want to use the provided class_files) or Java 7 (but you must compile the src yourself)

### To run (in terminal)
  1. Change directory to the *src* folder
  2. ```javac TSP.java```
  3. ```java TSP 100```

## Result Summary
Lower fitness (distance in this case) is better.
#### Brian
Average best fitness: 3767</br>
Best fitness: 3193
#### Jethro
Average best fitness: 4461</br>
Best fitness: 3860

## Normality Tests
Two different applications were used when determining normality, the online KS test provided in the slides (http://www.physics.csbsju.edu/stats/KS-test.n.plot_form.html) and SPSS 23 (since I will likely use it for various statistical tasks in my Honours project)

### Online KS test
#### Brian
KS finds the data is consistent with a normal distribution: P= 0.52 where the normal distribution has mean= 3753. and sdev= 193.0
#### Jethro
KS finds the data is consistent with a normal distribution: P= 0.80 where the normal distribution has mean= 4464. and sdev= 238.9

### SPSS
SPSS runs both the KS test and Shapiro-Wilk tests. The documentation states that KS tests are best for large datasets while Shapiro-Wilk is better for datasets < 2000.
The hypotheses for the tests are:</br>
Ho: The distribution of the data is normal</br>
Ha: The distribution of the data is not normal</br>

We will reject Ho if the p-value < 0.05
```
Tests of Normality
|------|-----------------------------|---------------------|
|      |Kolmogorov-Smirnova (a)      |Shapiro-Wilk         |
|      |-------------------|---|-----|------------|---|----|
|      |Statistic          |df |Sig. |Statistic   |df |Sig.|
|------|-------------------|---|-----|------------|---|----|
|Brian |.043               |100|.200*|.984        |100|.288|
|------|-------------------|---|-----|------------|---|----|
|Jethro|.060               |100|.200*|.987        |100|.433|
|----------------------------------------------------------|
 * This is a lower bound of the true significance.
 a Lilliefors Significance Correction
```

Since the Sig. (p-value) for each test is > 0.05 we accept Ho and assume the distribution of the datasets are normal.

## Two sampled t-tests
Since the datasets passed the normality tests, we can run two sampled t-tests to determine if there is a statistically significant difference between the mean of the two datasets.
Two different applications were used when running the t-tests, the t-test provided in the slides (http://www.physics.csbsju.edu/stats/t-test_bulk_form.html) and SPSS 23
The hypotheses for the tests are:</br>
Ho: The mean cost between the two datasets are the same</br>
Ha: The mean cost between the two datasets are different</br>

### Online t-test
t= -25.2</br>
sdev= 195.</br>
degrees of freedom =198 The probability of this result, assuming the null hypothesis, is less than .0001

### SPSS
```
Independent Samples Test
|--------------------------------|--------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------|
|                                |Levene's Test for Equality of Variances     |t-test for Equality of Means                                                                                                                  |
|                                |---------------------------------------|----|----------------------------|-------|---------------|---------------|---------------------|---------------------------------------------------|
|                                |F                                      |Sig.|t                           |df     |Sig. (2-tailed)|Mean Difference|Std. Error Difference|95% Confidence Interval of the Difference          |
|                                |                                       |    |                            |       |               |               |                     |-----------------------------------------|---------|
|                                |                                       |    |                            |       |               |               |                     |Lower                                    |Upper    |
|----|---------------------------|---------------------------------------|----|----------------------------|-------|---------------|---------------|---------------------|-----------------------------------------|---------|
|Cost|Equal variances assumed    |7.620                                  |.006|-25.201                     |198    |.000           |-694.0000      |27.5384              |-748.3062                                |-639.6938|
|    |---------------------------|---------------------------------------|----|----------------------------|-------|---------------|---------------|---------------------|-----------------------------------------|---------|
|    |Equal variances not assumed|                                       |    |-25.201                     |185.725|.000           |-694.0000      |27.5384              |-748.3282                                |-639.6718|
|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
```

Since the Sig. (p-value) is < 0.05 for both SPSS and the online t-test, we reject Ho and conclude that Brian's solution offers a significant reduction in cost over Jethro's solution.

## Discussion
My approach only uses mutation where Jethro's uses cut-and-crossfill between the best and a random chromosome between index 1 and 15 of the sorted list (inclusive). Those 100 children are then mutated using inversion. The best 100 of the children and parents are taken to the next generation. I do not feel crossover is that beneficial for this problem space. </br>

My approach is different, I focus on exploiting the best chromosome for the current landscape as much as possible. I therefore generate mutants from only the best chromosome, which may itself be a mutant created in the current evolutionary process, if it is deemed better than the current best parent (confirmed to be allowed by Dr. Nitschke). In addition, I only allow changes at 4 indices of the population in a generation. This is because I know the landscape is dynamic so what is good now may not be good when the landscape changes. I try keep the majority of the other population around so that I can find a very good local optima for each landscape instead of lots of optima for a specific landscape which may change in the next generation. My approach also ensures that we evaluate at most 100 mutates in a generation and ensures that the population never exceeds 100 (even temporarily).
