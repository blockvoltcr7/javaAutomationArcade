# Generics in Java: Lesson 1

## What are Generics?

Generics are a feature in Java that allows you to define classes, interfaces, and methods with a placeholder for types. This means you can create a single class or method that can operate on different data types while providing compile-time type safety.

## Type Parameters in Generics

The letters used in generics are called type parameters. While you can use any valid identifier, there are commonly used letters that follow conventions:

- **T** - "Type". The most commonly used type parameter. Used for any type, typically the first type parameter.
- **E** - "Element". Typically used for collection elements.
- **K** - "Key". Used for map keys.
- **V** - "Value". Used for map values.
- **N** - "Number". Used when working with numbers.
- **S,U,V etc.** - Used for multiple type parameters, representing different types.

## Benefits of Generics

1. **Type Safety**: Generics enable compile-time type checking, preventing runtime errors.
2. **Code Reusability**: Write code that works with different types without duplication.
3. **Type Casting Elimination**: No need for explicit type casting when retrieving elements.
4. **Generic Algorithms**: Implement algorithms that work with different types.

## Common Use Cases

1. Collections Framework
   - List<String>, Set<Integer>, Map<String, User>
2. Custom Data Structures
   - Binary Trees, Linked Lists
3. Utility Classes
   - Optional<T>, Future<T>
4. Method Parameters and Return Types

