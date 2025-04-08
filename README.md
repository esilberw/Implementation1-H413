Project Implementation #1 - Heurisitc Optimization INFO-H413 - SILBERWASSER ELLIOT 518397
 
# USAGE
NOTE: In the directory of this project, you already have the .class file to launch directly thre project. Not need to compile it.

- To launch a specific algorithm on a specific instance without testing:
  For the exercice 1.1:
  
  $java IterativeImprovement ./Benchmarks/taXXX --<pivoting_rule> --<neighborhood> --<initializing_method>
 
  For the exercice 1.2:

  $java IterativImprovement ./Benchamrks/taXXX --vnd --<neighborhoods_order>

  Where <neighborhoods_order> is either --one or --two corresponding to the order described in the technical descritpion pdf report of the project.

- To launch a test on a specific algorithm over all instances:

  For the exercice 1.1:

  $java IterativeImprovement --test --<pivoting_rule> --<neighborhood> --<initializing_method>

  For the exercice 1.2:

  $java IterativeImprovement --test --vnd --<neighborhoods_order>

  
