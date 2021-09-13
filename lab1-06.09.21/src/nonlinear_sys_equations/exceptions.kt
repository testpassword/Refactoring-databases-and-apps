package nonlinear_sys_equations

class CannotSolveException: Exception("Данную СЛАУ невозможно решить методом Гаусса!")

class EndlessSolutionException: Exception("Данная СЛАУ имеет бесконечное множество решений!")

class InvalidMatrixLineException: Exception("Количество элементов расширенной матрицы не соответствует действительному!")

class WrongSizeException: Exception("Количество уравнений должно быть положительным и меньше 20.")