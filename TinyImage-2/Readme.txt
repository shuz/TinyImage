��javaʵ�֣�GUIʹ��swing. 

output\
  ��Ӧ��class�ļ� main������TinyImageApp.class��


tinyimage\
(no changes)

tinyimage\morphology\

  KernelFilter.java
    �����˴����morphological���㣬����dilate, erode, opening, closing, skeletonize, 
skeleton restore��distance transform, ����skeletonize��2��ʵ�֣�skeleton��skeletonByDistance��ǰ����skeleton subsetʵ�֣�������distanceͼ�ľֲ����ֵ��ʵ�֣��Һ��߱����ֲ�����ֵ���Ӷ�����������restoration. Edge Detect��image - erode(image)��ʵ�֣���MorphologyController��ֱ��ʵ�֡�
    
    ��ʵ����gray scale reconstruction, �Լ�gradient��conditional dilation.



ע��ֱ��ʵ��skeleton subsetʱ�����������й�ϵ��

E(A, kB) = E(A, D((k-1)B, B)) = E(E(A, (k-1)B), B)
�Ӷ���E(A, kB)���Դ�k=0��ʼ��������õ�

E(A, kB) - O(E(A, kB), B)
= E(A, kB) - D(E(E(A, kB), B), B)
= E(A, kB) - D(E(A, (k+1)B), B)
= img[k] - D(img[k+1], B)

��ˣ�ֻ��Ҫ��סE(A, kB)�������е�fimage����E(A, (k+1)B)�������е�ffimage�����ɡ�
