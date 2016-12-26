//package utot.utot.poem;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Color;
//import android.graphics.drawable.ColorDrawable;
//import android.os.Build;
//import android.os.Environment;
//import android.support.percent.PercentRelativeLayout;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.bumptech.glide.GenericRequestBuilder;
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.Priority;
//import com.bumptech.glide.load.ResourceDecoder;
//import com.bumptech.glide.load.data.DataFetcher;
//import com.bumptech.glide.load.engine.Resource;
//import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
//import com.bumptech.glide.load.model.ModelLoader;
//import com.bumptech.glide.load.resource.bitmap.BitmapEncoder;
//import com.bumptech.glide.load.resource.bitmap.BitmapResource;
//import com.bumptech.glide.load.resource.bitmap.StreamBitmapDecoder;
//import com.bumptech.glide.load.resource.drawable.GlideDrawable;
//import com.bumptech.glide.load.resource.file.FileToStreamDecoder;
//import com.bumptech.glide.load.resource.transcode.BitmapToGlideDrawableTranscoder;
//import com.facebook.login.widget.LoginButton;
//
//import java.io.File;
//import java.io.IOException;
//
//import io.realm.RealmResults;
//import utot.utot.R;
//import utot.utot.customobjects.Poem;
//import utot.utot.customobjects.PoemPicture;
//import utot.utot.customviews.ButtonPlus;
//import utot.utot.helpers.BitmapMaker;
//import utot.utot.helpers.FinalVariables;
//
///**
// * Created by elysi on 12/24/2016.
// */
//class PoemGlideAdapter extends RecyclerView.Adapter<PoemGlideAdapter.ViewHolder> {
//    // See https://docs.google.com/drawings/d/1KyOJkNd5Dlm8_awZpftzW7KtqgNR6GURvuF6RfB210g/edit?usp=sharing
//    //                                  ModelType/A,    DataType/T,     ResourceType/Z, TranscodeType/R
//    private final GenericRequestBuilder<GenerateParams, GenerateParams, Bitmap, GlideDrawable> generator;
//    private RealmResults<PoemPicture> poemPictures;
//    private Context context;
//    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
//        public TextView poemView;
//        public ImageView backgroundPic;
//        public ImageButton deleteButton;
//        public ButtonPlus shareButton;
//        public LoginButton loginButton;
//        public PercentRelativeLayout displayImg;
//        public ViewHolder(View view) {
//            super(view);
//
////            poemView = (TextView) view.findViewById(R.id.poem);
//            backgroundPic = (ImageView) view.findViewById(R.id.poemPic);
//            deleteButton = (ImageButton) view.findViewById(R.id.deleteButton);
//            shareButton = (ButtonPlus) view.findViewById(R.id.shareButton);
////            displayImg = (PercentRelativeLayout) view.findViewById(R.id.displayImg);
//            loginButton = (LoginButton) view.findViewById(R.id.login_fb);
//
//            view.setOnClickListener(this);
//
//        }
//
//
//        @Override
//        public void onClick(View v) {
//            Intent clicked = new Intent(context, ClickPoem.class);
//            clicked.putExtra("POEM_POS", getAdapterPosition());
//            context.startActivity(clicked);
//        }
//    }
//
//    public PoemGlideAdapter(final Context context, RealmResults<PoemPicture> poemPictures) {
//        this.context = context;
//        this.poemPictures = poemPictures;
//        generator = Glide // this part should be cleaner in Glide 4.0, but that's not released yet
//                .with(context)
//                .using(new GenerateParamsPassthroughModelLoader(), GenerateParams.class)          // custom class
//                .from(GenerateParams.class)
//                .as(Bitmap.class)
//                .transcode(new BitmapToGlideDrawableTranscoder(context), GlideDrawable.class)     // builtin
//                .decoder(new GenerateParamsBitmapResourceDecoder(context))                        // custom class
//                .encoder(new BitmapEncoder(Bitmap.CompressFormat.PNG, 0/*ignored for lossless*/)) // builtin
//                .cacheDecoder(new FileToStreamDecoder<Bitmap>(new StreamBitmapDecoder(context)))  // builtin
//                //.placeholder(new ColorDrawable(Color.YELLOW)) // you can pre-set placeholder and error
//                .error(new ColorDrawable(Color.WHITE))            // so it's easier when binding
//        //.diskCacheStrategy(DiskCacheStrategy.NONE)    // only for debugging to always regenerate
//        //.skipMemoryCache(true)                        // only for debugging to always regenerate
//        ;
//    }
//    @Override public int getItemCount() { return poemPictures.size(); }
//
//    @Override
//    public void onBindViewHolder(PoemGlideAdapter.ViewHolder holderT, int position) {
//        final PoemGlideAdapter.ViewHolder holder = holderT;
//        final PoemPicture poem = poemPictures.get(position);
//        GenerateParams params = new GenerateParams(poem);
//        generator/*.clone() in case you see weird behavior*/.load(params).into(holder.backgroundPic);
//    }
//    @Override
//    public PoemGlideAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View itemView = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.item_poem, parent, false);
//
//        return new PoemGlideAdapter.ViewHolder(itemView);
//    }
//
//}
//
//
//class GenerateParams {
//    public final PoemPicture poem;
//
//    public GenerateParams(PoemPicture poem) {
//        this.poem = poem;
//    }
//
//    public String getId() {
//        // TODO make sure it's unique for every possible instance of GenerateParams
//        // because it will affect how the resulting bitmap is cached
//        // the below is correct correct for the current fields, if those change this has to change
//        return Integer.toString(poem.getPrimaryKey());
//    }
//}
//
///** Boilerplate because of the degeneration in ModelType == DataType, but important for caching.
// */
//class GenerateParamsPassthroughModelLoader implements ModelLoader<GenerateParams, GenerateParams> {
//    @Override public DataFetcher<GenerateParams> getResourceFetcher(final GenerateParams model, int width, int height) {
//        return new DataFetcher<GenerateParams>() {
//            @Override public GenerateParams loadData(Priority priority) throws Exception { return model; }
//            @Override public void cleanup() { }
//            @Override public String getId() { return model.getId(); }
//            @Override public void cancel() { }
//        };
//    }
//}
//
///** Handles pooling to reduce/prevent GC lagging from too many {@link Bitmap#createBitmap}s */
//class GenerateParamsBitmapResourceDecoder implements ResourceDecoder<GenerateParams, Bitmap> {
//    private final Context context;
//    public GenerateParamsBitmapResourceDecoder(Context context) { this.context = context; }
//    @Override public Resource<Bitmap> decode(GenerateParams source, int width, int height) throws IOException {
//        BitmapPool pool = Glide.get(context).getBitmapPool();
////        Bitmap bitmap = pool.getDirty(width, height, Bitmap.Config.ARGB_8888);
////        if (bitmap == null) {
////            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
////        }
//        String root;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            root = context.getExternalFilesDirs(null).toString();
//        } else{
//            root = Environment.getExternalStorageDirectory().getAbsolutePath();
//        }
//        File file = new File(root+ File.separator+ FinalVariables.FOLDER_NAME, source.poem.getFilename());
//        Bitmap bmp = BitmapFactory.decodeFile(file.getAbsolutePath());
//        return BitmapResource.obtain(bmp, pool);
//    }
//    @Override public String getId() {
//        // be careful if you change the Generator implementation you have to change this
//        // otherwise the users may see a cached image; or clear cache on app update
//        return "com.example.MyImageGenerator";
//    }
//}
//
//
//
