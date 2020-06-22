1. to build/compile the core/facade
ant compile

2. run a model with default configuration file
ant -Dtest.model=tcgen_model test

- above command will generate test case and execute against an implementation w/ default configuration file of "tcgen_model.config.json"

3. run a model by given configuration file
ant -Dtest.model=tcgen_model -Dconfig.file=/home/norechang/workspace/tcgen/tcgen-uml2clp/tcgen_model/tcgen_model.config.json compile test

4. run regression tests w/ default configureation for all models
ant regression

5. to generate mutation coverage.
ant pit
