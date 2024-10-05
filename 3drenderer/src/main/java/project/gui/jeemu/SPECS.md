## JOHNEngine Easy Markup Language (Jeemu)

This markup language is designed for describing UIs within the JOHNEngine renderer.

### Body
A Jeemu-document must specify a body for the UI which will function as the root node for the
UI graph. Much like in HTML, the body can contain an arbitrary number of nested elements, such
as divs, buttons and texts. For each element, a numer of properties, that describe the style of 
the element, can be assigned. These properties function the same way as CSS-properties in that 
they are cascaded and applied to the child elements. In the background, each element has the 
same set of properties, however, only the ones that have been specified in the Jeemu-document 
will be applied to the element, while the unspecified properties will either be inherited from 
the parent or set to default values.

### Data types
There are three main data types: color, string, numeric. **Color** values can either be in a 
hexadecimal format (just like in CSS) or constructed via the built-in rgb()- and rgba()-
functions. **Strings** work just like in any other language. They must begin with either a 
single quote ', a double quote ", or a backtick `, and end with the same character. These 
characters can also be escaped. 

**Numeric** values are floating point values that may NOT begin with the decimal 
point. Numeric values can be either entered as pixels, which denote absolute pixel values, 
percentages, which are portions of either the parent element or the element itself, columns/
rows, which denote multiples of columns/rows in the grid of the parent element, or simply as 
plain numbers.

Additionally, property values can be provided or evaluated on-the-fly. The **theme property 
getter function**, `t("section.section.propertyName")` or `theme("section.propertyName")`, 
can be used to get the value stored in a given property of the currently active theme. 
On-the-fly evaluation can be achieved using the **evaluation function**, `e(50% + 5px)` or 
`expr(rgba(255, 0, 0, 255))`. Express evaluation is not type-safe as expr may return any type
of result, and will default to a numeric value or a color value if the evaluation fails.

Example with two nested divs:

    body {
      div {
        width: 400px; // Pixel value
        height: 400px;  // Pixel value
        backgroundColor: e(rgba(255, 0, 0, 255)); // Evaluates to a red color value
        columns: 10; // Plain number
        rows: 5; // Plain number

        div {
          left: 1c; // Number of columns in parent's grid
          top: 0r; // Number of rows in parent's grid
          width: 90%; // Percentage of parent's width
          height: e(max(100px, 75%)); // Returns the max of 100 pixels and 75% of parent's height
          backgroundColor: theme("colors.major.background"); // Gets 'background'-property from active theme
        }
      }
    }

### Themes
Jeemu allows you to specify different themes whose properties can be referenced throughout the 
elements. Themes consist of key-property-pairs as well as sections that consist of key-property-
pairs. A section is like a sub-theme within the theme. Each property declaration must begin with 
a data type (string, color or numeric), followed by the property key, ending in the property's
value. The theme declaration itself must begin with the name of the theme (in camel case) 
followed by the "theme"-keyword. Themes must be declared before being used, and can only be 
declared within the document-scope. 

For example:

    casual theme {
      sectionName {
        string customStringProp: "testing";
      }

      sectionName2 {
        color customColorProp: rgba(0, 0, 0, 255);
        
        subSectionOf2 {
          numeric customNumericProp: 1c;
        }
      }
    }

### Collections
Collections are essentially custom elements that are derived from the built-in ones. A 
collection pre-defines the element's properties and children. Collections must be declared 
before being used, and can only be declared within the document-scope. The declaration must 
begin with the name of the collection followed by the "collection as"-keywords, and ended with
the keyword of the element that the collection is to be based on. Once declared, the 
collection can be used inside the body or inside other collections.

For example:

      // Declares a custom div called 'custom'
    custom collection as div {
      width: 100px;
      height: 100px;
      backgroundColor: #FFFFFF;
    }

    body {
      div {
        width: 50%;
        height: 50%;

          // Use the 'custom' collection
        custom {
          width: 200px; // Sets the width overriding the pre-defined value
          primaryColor: #000000;

            // Adds a text child into the div
          text {
            Hello world!
          }
        }
      }
    }

### Responsive styles
Just like in CSS, responsive styles can be specified by using a query. In Jeemu, these queries 
are called R-queries or responsivness queries. R-queries are specified using the **@responsive**-
keyword followed by a block. Within the block, you must specify the conditions under which the 
style is to take effect. Unlike CSS, Jeemu supports a very limited set of responsivness 
criteria: **@window** and **@ratio**.

**@window**-criteria must be followed by the window resolution where the width and the height
are separated via the character x (e.g. `800x600`). This tells the UI to apply this style to 
all the properties defined in the criteria block IF the width and the height of the window 
are equal or less than the given values. If there are multiple criteria, the one that is 
specified first will take precedence. It is therefore adviced to list the criteria from the 
strictest to the least strict. If a property is listed within one criteria but not another,
the first criteria (that is satisfied) where that property is encountered will be applied.

**@ratio**-criteria must be followed by the window's aspect ratio where the numerator and 
the denominator are separated via a colon (e.g. `16:9`). This indicates that the style will 
be applied to the element IF the aspect ratio of the window (width/height) is equal or less 
than the given ratio. Once again, criteria will be applied from strictest to least strict.

Example with two divs in a body, one with two styles that respond to window dimensions, and
one with a single style that responds to window's aspect ratio:

    body {
      div {
          // Default settings
        width: 50%;
        height: 50%;

        @responsive {
            // If window is 400x400 or smaller, these properties are applied
          @window 400x400 {
            height: 100%;
          }

            // If window is 800x600 or smaller, these properties are applied
          @window 800x600 {
            width: 100%;
          }
        }
      }
      div {
        width: 100px;
        height: 62px;

          // If window's aspect ratio is 1:1 or smaller, these properties are applied
        @responsive {
          @ratio 1:1 {
            width: 100%;
          }
        }
      }
    }
