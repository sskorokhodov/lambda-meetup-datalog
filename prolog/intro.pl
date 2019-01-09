%%% Facts

parent(alice, charlie).
parent(bob, charlie).
parent(charlie, dave).

sex(bob, male).
sex(alice, female).
sex(charlie, male).
sex(dave, male).

%%% Rules

%% <head> :- <body>.

mother(Parent, Child) :-
  parent(Parent, Child),
  sex(Parent, female).

father(Parent, Child) :-
  parent(Parent, Child),
  sex(Parent, male).

%%% Recursion

ancestor(Parent, Child) :-
  parent(Parent, Child).

ancestor(A, B) :-
  parent(A, Child),
  ancestor(Child, B).