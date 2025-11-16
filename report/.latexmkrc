$pdf_mode = 1;  # Generate PDF
$pdflatex = 'pdflatex -interaction=nonstopmode -synctex=1 %O %S';
$bibtex_use = 2;
$out_dir = '/build';
$jobname = "report";

$cleanup_mode = 1;
$clean_ext = 'aux log toc lof lot bbl blg idx ind ilg out nav snm vrb fls fdb_latexmk synctex.gz';
@generated_exts = ('aux', 'log', 'toc', 'lof', 'lot', 'bbl', 'blg', 
                   'idx', 'ind', 'ilg', 'out', 'nav', 'snm', 'vrb',
                   'fls', 'fdb_latexmk', 'synctex.gz', 'synctex.gz(busy)',
                   'run.xml', 'bcf', 'xdv');