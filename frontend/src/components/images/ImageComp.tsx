
import React, { CSSProperties, ChangeEventHandler, useState } from "react";
import { MdHideImage } from "react-icons/md";
import "./ImageComp.css";

export type ImageProps = {
    name: string;
    src: string;
    alt?: string;
    title?: string;
    classes? : string;
    containerClasses? : string;
    style?: CSSProperties;
    containerStyle?: CSSProperties;
    onClickFunction?: ChangeEventHandler<HTMLInputElement>;
}


const ImageComp: React.FC<ImageProps> = (props: ImageProps) => {
    const [isLoaded, setLoaded] = useState<boolean>(false);
    const [error, setError] = useState<string | null>(null);
    const URL_SPLITTED = props.src.split("/");
    const FILE_NAME = URL_SPLITTED[URL_SPLITTED.length - 1];

    let alt = props.alt ? props.alt : FILE_NAME;
    let title = props.title ? props.title : FILE_NAME;
    
    let classes = props.classes === "undefined" ? "" : props.classes;

    let fileLocation = props.src;

    let imageLoaded: React.ReactEventHandler<HTMLImageElement> = () => {
        setLoaded(true);
    }

    let imageError: React.ReactEventHandler<HTMLImageElement> = () => {
        setLoaded(true);
        setError("makka nem található")
    }

    let containerClasses: string = "imgContainer " + (props.containerClasses ? props.containerClasses : "");
    let hideIconSize: number = 50;

    return(
        <div className={containerClasses} style={props.containerStyle}>
            {
                !error ?
                <img id={props.name}
                     src={fileLocation}
                     alt={alt}
                     title={title}
                     className={classes}
                     style={props.style}
            
            
                     onLoad={imageLoaded}
                     onError={imageError}
                />
                :
                    <div className="imgError" aria-label="asd">
                        <MdHideImage size={hideIconSize}/>
                        {alt}
                    </div>

            }
            {
                !isLoaded ?
                    <div className="imgLoader" /> 
                :
                    ""
            }
        </div>
    )
}

export default ImageComp;