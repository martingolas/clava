typedef char base;

int foo() {	
	return 2;
}

float* fooPointer(float* aPointer) {
	return aPointer;
}

typedef double aType;

aType fooTypedef() {
	return 2.0;
}

void * polybench_alloc_data(unsigned long long n, int elt_size);

void vlatypes(int ni, int a[3][ni]) {}

typedef void(*function_prt_with_vla) (int ni,int nj,double tmp[ni + 0][nj + 0]);

int main() {
	float* aFloatPointer;
	
	int fooResult = foo();
	float * fooPointerResult = fooPointer(aFloatPointer);
	aType fooTypedefResult = fooTypedef();

	int n = 10;
	base (*seq)[n + 0];
	seq = (base(*)[n + 0])polybench_alloc_data (n + 0, sizeof(base));
}
