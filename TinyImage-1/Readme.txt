��javaʵ�֣�GUIʹ��swing. 
(ע�����ϻػ����Ͻṹ�����޸ģ����˴���д���뱾���йصĲ���)

output\
  ��Ӧ��class�ļ� main������TinyImageApp.class��


tinyimage\

  TinyImageApp.java
    ֻ��һ��main����


  TinyImage.java
    �����ȡ����ʾͼƬ��
    ���ϻص�Histogramģ���Ѿ�������tinyimage\histogram\�£�


  ImageController.java
    TinyImage�ĸ���ģ��Ľӿڡ�
    Ŀǰ����tinyimage\histogram\HistogramController.java��
            tinyimage\spatialfilter\SpatialFilterController.java


tinyimage\spatialfilter\

  SpatialFilter.java
    public int filterPixel(int x, int y)
      ���㵥�����صľ����3��channel�ֿ����㣩

    public BufferedImage filter()
      ��������ͼ��ľ��

    �ڶ�ȡͼ��߽������ʱ���ñ߽�������ĵ���ơ�


  SpatialFilterController.java
    TinyImage�ڶ�������ģ�顣�����м���Gaussian Kernel�ȵĴ��룺
      private JButton getJGaussKernel();
    ����������ж����˸ð�ť��ʱ�䴦����������м���Gaussian Kernel�Ĵ��롣
    ����Kernel�ļ�����������Ӧ�ĺ����ڡ�


  JKernelPanel.java
    �Զ���ؼ���������ʾKernel�ȡ�
