data = {
    const imports = await FileAttachment("cluster@5.json").json();
  
    const indexByName = new Map;
    const nameByIndex = new Map;
    const matrix = [];
    let n = 0;
  
    // Returns the Flare package name for the given class name.
    function name(name) {
      return name.substring(0, name.lastIndexOf(".")).substring(6);
    }
  
    // Compute a unique index for each package name.
    imports.forEach(d => {
      if (!indexByName.has(d = name(d.name))) {
        nameByIndex.set(n, d);
        indexByName.set(d, n++);
      }
    });
  
    // Construct a square matrix counting package imports.
    imports.forEach(d => {
      const source = indexByName.get(name(d.name));
      let row = matrix[source];
      if (!row) row = matrix[source] = Array.from({length: n}).fill(0);
      d.imports.forEach(d => row[indexByName.get(name(d))]++);
    });
  
    return {
      matrix,
      indexByName,
      nameByIndex
    };
  }