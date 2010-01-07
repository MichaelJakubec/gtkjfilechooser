#include <stdio.h>
#include <gtk/gtk.h>

typedef struct {
	char *name;
	char *vorname;
	int alter;
} Person;

void print_person_g(gpointer data) {
	Person *p;
	p = data;

	char *vorname;
	vorname = p->vorname;

	char *name;
	name = p->name;

	int alter;
	alter = p->alter;

	printf("gpointer: %s %s ist %d Jahre alt\n", vorname, name, alter);
}

void print_person_p(Person p) {
	printf("Person:   %s %s ist %d Jahre alt\n", p.vorname, p.name, p.alter);
}

int main(void) {
	char *vorname = "Cerbo";
	char *name = "Costantino";
	int alter = 33;

	Person pp = { vorname, name, alter };
	print_person_g((gpointer) &pp);
	print_person_p(pp);

	return 0;
}
