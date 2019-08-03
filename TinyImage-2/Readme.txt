用java实现，GUI使用swing. 

output\
  对应的class文件 main函数在TinyImageApp.class中


tinyimage\
(no changes)

tinyimage\morphology\

  KernelFilter.java
    定义了大多数morphological运算，包括dilate, erode, opening, closing, skeletonize, 
skeleton restore和distance transform, 其中skeletonize有2个实现：skeleton和skeletonByDistance，前者用skeleton subset实现，后者用distance图的局部最大值来实现，且后者保留局部最大的值，从而可以用来做restoration. Edge Detect用image - erode(image)来实现，在MorphologyController中直接实现。
    
    还实现了gray scale reconstruction, 以及gradient和conditional dilation.



注：直接实现skeleton subset时，利用了下列关系：

E(A, kB) = E(A, D((k-1)B, B)) = E(E(A, (k-1)B), B)
从而，E(A, kB)可以从k=0开始迭代运算得到

E(A, kB) - O(E(A, kB), B)
= E(A, kB) - D(E(E(A, kB), B), B)
= E(A, kB) - D(E(A, (k+1)B), B)
= img[k] - D(img[k+1], B)

因此，只需要记住E(A, kB)（程序中的fimage）和E(A, (k+1)B)（程序中的ffimage）即可。
