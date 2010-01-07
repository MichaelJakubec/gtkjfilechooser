#include <stdio.h>
#include <gtk/gtk.h>

typedef struct {
	char name[20];
	char vorname[20];
	int alter;
} Person;

void print_person(gpointer data) {
	Person p = (Person) &data;
	printf("-> %s %s ist %d Jahre alt\n", p.vorname, p.name, p.alter);
}
int main(void) {

	Person pp = { "Cerbo", "Costantino", 33 };
	print_person(pp);

	return 0;
}
