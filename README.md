# CodeRecognition

I worked on this app as a part of my Bachelor project. The goal was to detect code written on a paper and compile it. To help with localization on a paper, I divided each paper into cells. Using **Hough transform** algorithm, I extracted coordinates of each cell. The content of each cell then went through my ANN, which I pretrained on MNIST dataset.

Unfortunately, the results were not very satisfying. I achieved success rate of around 50% characters